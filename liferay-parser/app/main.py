import logging
import os
from fastapi import FastAPI
from pydantic import BaseModel
from liferay.dml.dml_extraction import DmlExtraction
from liferay.calendar.calendar_extraction import CalendarExtraction
from liferay.user.user_extraction import UserExtraction


app = FastAPI()

ingestion_url = os.environ.get("INGESTION_URL")
if ingestion_url is None:
    ingestion_url = "http://ingestion:8080/v1/ingestion/"

delete_url = os.environ.get("DELETE_URL")
if delete_url is None:
    delete_url = "http://index-writer:8080/v1/delete-data-documents"

log_level = os.environ.get("INPUT_LOG_LEVEL")
if log_level is None:
    log_level = "INFO"

logger = logging.getLogger("uvicorn.access")


class LiferayRequest(BaseModel):
    domain: str
    username: str
    password: str
    timestamp: int
    datasourceId: int


@app.post("/get-dml")
def get_documents(request: LiferayRequest):

    request = request.dict()

    domain = request['domain']
    username = request["username"]
    password = request['password']
    timestamp = request["timestamp"]
    datasource_id = request["datasourceId"]

    dml_extraction = DmlExtraction(domain, username, password, timestamp, datasource_id, ingestion_url)

    dml_extraction.extract()

    return


@app.post("/get-calendars")
def get_calendars(request: LiferayRequest):

    request = request.dict()

    domain = request['domain']
    username = request["username"]
    password = request['password']
    timestamp = request["timestamp"]
    datasource_id = request["datasourceId"]

    calendar_extraction = CalendarExtraction(domain, username, password, timestamp, datasource_id, ingestion_url)

    calendar_extraction.extract_recent()

    return


@app.post("/get-users")
def get_users(request: LiferayRequest):

    request = request.dict()

    domain = request['domain']
    username = request["username"]
    password = request['password']
    timestamp = request["timestamp"]
    datasource_id = request["datasourceId"]

    user_extraction = UserExtraction(domain, username, password, timestamp, datasource_id, ingestion_url)

    user_extraction.extract_recent()

    return
