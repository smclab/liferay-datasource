FROM python:3.8

COPY ./liferay-datasource/liferay-parser/requirements.txt /requirements.txt

RUN pip install -r requirements.txt

COPY ./liferay-datasource/liferay-parser/app /app

WORKDIR /app

CMD ["uvicorn", "main:app", "--reload", "--host", "0.0.0.0", "--port", "5000"]