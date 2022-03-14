"""
Copyright (c) 2020-present SMC Treviso s.r.l. All rights reserved.

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU Affero General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU Affero General Public License for more details.

You should have received a copy of the GNU Affero General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
"""

import logging
from datetime import datetime
from requests.auth import HTTPBasicAuth
from logging.config import dictConfig
import json
import time
import requests
from ..util.utility import call_extraction_api, post_message
from ..util.log_config import LogConfig

dictConfig(LogConfig().dict())

N_RETRY = 10
RETRY_TIMEOUT = 10
TIMEOUT = 10
N_MAX_ERRORS = 30
SIZE = 200


class UserExtraction:

    def __init__(self, domain, username, password, timestamp, datasource_id, ingestion_url):

        super(UserExtraction, self).__init__()
        self.domain = domain
        self.username = username
        self.password = password
        self.timestamp = timestamp
        self.datasource_id = datasource_id
        self.ingestion_url = ingestion_url

        self.status_logger = logging.getLogger("mycoolapp")

        self.url_extract_all = self.domain + "/o/dml-exporter/users"
        self.url_recent_count = self.domain + "/o/dml-exporter/users/modifiedCount/" + str(timestamp)
        self.url_extract_recent = self.domain + "/o/dml-exporter/users/modified/" + str(timestamp) + "/"

        self.basic_auth = HTTPBasicAuth(self.username, self.password)

        self.n_retry = N_RETRY
        self.retry_timeout = RETRY_TIMEOUT
        self.timeout = TIMEOUT
        self.n_max_errors = N_MAX_ERRORS
        self.size = SIZE

    def manage_data(self, users):

        users_number = 0
        end_timestamp = datetime.utcnow().timestamp() * 1000

        self.status_logger.info("Posting users")

        for user in users:
            try:
                user_values = {
                    "userId": user['userId'],
                    "screenName": user['screenName'],
                    "emailAddress": user['emailAddress'],
                    "jobTitle": user['jobTitle'],
                    "male": user['male'],
                    "twitterSn": user['twitterSn'],
                    "skypeSn": user['skypeSn'],
                    "facebookSn": user['facebookSn'],
                    "firstName": user['firstName'],
                    "middleName": user['middleName'],
                    "lastName": user['lastName'],
                    "fullName": user['firstName'] + " " + user['lastName'],
                    "birthday": user['birthday'],
                    "portrait_preview": user["portraitContent"]
                }

                addresses_list = user["addresses"]

                raw_content = user['firstName'] + " " + user['lastName'] + " <" + user[
                    'emailAddress'] + ">"

                if len(addresses_list) > 0:
                    address_info = addresses_list[0]
                    user_values["street"] = address_info["street"]
                    user_values["zip"] = address_info["zip"]
                    user_values["city"] = address_info["city"]
                    user_values["country"] = address_info["country"]

                    raw_content = raw_content + " " + address_info["city"] + " " + address_info["country"]

                phones_list = user["phoneNumbers"]
                if len(phones_list) > 0:
                    user_values["phoneNumber"] = phones_list[0]

                datasource_payload = {"user": user_values}

                payload = {
                    "datasourceId": self.datasource_id,
                    "contentId": str(user['userId']),
                    "parsingDate": int(end_timestamp),
                    "rawContent": raw_content,
                    "datasourcePayload": datasource_payload,
                    "resources": {
                        "binaries": []
                    }
                }

                try:
                    # self.status_logger.info(datasource_payload)
                    post_message(self.ingestion_url, payload, self.timeout)
                    users_number = users_number + 1
                except requests.RequestException:
                    self.status_logger.error("Problems during posting of users with "
                                             + str(user['userId']))
                    continue

            except json.decoder.JSONDecodeError:
                continue

        self.status_logger.info("Posting ended")
        self.status_logger.info("Have been posted " + str(users_number) + " users")

        return users_number

    def extract_all(self):

        try:
            users = call_extraction_api(self.url_extract_all, self.basic_auth,
                                        self.timeout, self.n_retry, self.retry_timeout)
        except requests.RequestException:
            self.status_logger.error("No users extracted. Extraction process aborted.")
            return

        self.status_logger.info("Getting all users")

        users = json.loads(users)

        self.status_logger.info("Extraction ended")
        self.status_logger.info("Have been extracted " + str(len(users)) + " users")

        self.manage_data(users)
        return

    def extract_recent(self):
        try:
            n_users = call_extraction_api(self.url_recent_count, self.basic_auth,
                                          self.timeout, self.n_retry, self.retry_timeout)
        except requests.RequestException:
            self.status_logger.error("No users extracted. Extraction process aborted.")
            return

        n_users = int(json.loads(n_users))
        extracted_users = 0
        posted_users = 0

        self.status_logger.info("Starting the extraction process of " + str(n_users) + " users")

        for start in range(0, n_users, self.size):
            try:
                users = call_extraction_api(self.url_extract_recent + str(start) + "/" + str(start + self.size),
                                            self.basic_auth, self.timeout, self.n_retry,
                                            self.retry_timeout)
            except requests.RequestException:
                self.status_logger.error("Error during api call. Extraction process aborted.")
                self.status_logger.error("Have been extracted " + str(extracted_users) + " users")
                break

            users = json.loads(users)
            extracted_users += len(users)

            self.status_logger.info("Have been extracted " + str(len(users)) + " users")

            posted_users += self.manage_data(users)

        self.status_logger.info("Extraction ended")
        self.status_logger.info("In total have been extracted " + str(extracted_users) + " users")
        self.status_logger.info("In total have been posted " + str(posted_users) + " users")
        return
