# Take #
Easily find dance classes in NYC.

## Build & Run ##
Download the ChromeDriver from here
https://sites.google.com/a/chromium.org/chromedriver/downloads. Note where you
saved the file.

```sh
$ export CHROME_WEBDRIVER_PATH=<path to your chrome webdriver>
$ cd take
$ sbt
> jetty:start
> browse
```

If `browse` doesn't launch your browser, manually open [http://localhost:8080/](http://localhost:8080/) in your browser.


## Deploying on Heroku ##

Set config variable for key CHROME_WEBDRIVER_PATH. This should be the location
where the buildpack installs the chromedriver executable. (This is needed for
Selenium to run).

## Stack ##

This web app was built using Scalatra. Web scraping and parsing was done using
Jsoup and Selenium. Bootstrap is used for prettifying the page and basic jquery
is done to make requests to the web server.
