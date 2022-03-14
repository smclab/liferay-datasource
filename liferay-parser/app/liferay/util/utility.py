import time
import base64
import requests
import logging
import json
from PIL import Image
from io import BytesIO
from logging.config import dictConfig
from ..util.log_config import LogConfig

dictConfig(LogConfig().dict())

logger = logging.getLogger("mycoolapp")


def post_message(url, payload, timeout=20):

    try:
        r = requests.post(url, json=payload, timeout=timeout)
        if r.status_code == 200:
            return
        else:
            r.raise_for_status()
    except requests.RequestException as e:
        logger.error(str(e) + " during request at url: " + str(url))
        raise e


def call_extraction_api(url, basic_auth, timeout=20, n_retry=10, retry_timeout=10):
    for i in range(n_retry):
        try:
            r = requests.get(url, auth=basic_auth, timeout=timeout)
            if r.status_code == 200:
                return r.text
            else:
                r.raise_for_status()
        except requests.RequestException as e:
            logger.warning("Retry number " + str(i) + " " + str(e) + " during request at url: " + str(url))
            if i < n_retry - 1:
                time.sleep(retry_timeout)
                continue
            else:
                logger.error(str(e) + " during request at url: " + str(url))
                raise e


try:
    with open("./liferay/util/mapping_config.json") as config_file:
        config = json.load(config_file)
        type_mapping = config["TYPE_MAPPING"]
except (FileNotFoundError, json.decoder.JSONDecodeError):
    logger.error("Ingestion configuration file is missing or there is some error in it.")


def map_type(content_type):
    try:
        document_type = type_mapping[content_type]
        return document_type
    except KeyError:
        return None
