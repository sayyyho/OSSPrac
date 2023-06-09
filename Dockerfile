FROM tiangolo/uwsgi-nginx-flask:python3.9

# WORKDIR /app

COPY . /app

CMD [ "python, "./main.py" ]