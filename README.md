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

