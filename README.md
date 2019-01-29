# LearnViro

A visual interactive language learning app, developed during [NWHacks 2019](https://devpost.com/software/nwhacks2019-jpnyvk)

### How It Works

Take a picture of anything, and 4 word choices in the language you are learning will appear. Select the correct choice that matches the picture!

![LearnViro](https://i.imgur.com/y53aNJO.png)

## Installation Instructions

In Server/words-server, run

```
mvn clean install
```

```
sudo docker build -t words-server .
```

```
sudo docker run -it --name wserver -e "GOOGLE_APPLICATION_CREDENTIALS={PATHTO CREDENTIALS}" -v {YOUR CREDENTIALS FILE}:{YOUR CREDENTIALS FILE} -p 8080:8080 -t words-server
```

Update the api path in the android app to the ip of your server and build the app

### Technologies Used

- Google Cloud Platform
    - Google Compute Engine
    - Google App Engine
    - Google Vision API
    - Google Translate API
- Datamuse API