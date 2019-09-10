const assert = require('assert')
const puppeteer = require('puppeteer')
const request = require('request-promise');
// request.debug = true

async function setup() {
  console.log("start")
  const browser = await puppeteer.launch({
    executablePath: 'google-chrome-unstable',
    args: ['--disable-dev-shm-usage', '--no-sandbox']
  });
  console.log("browser started")

  const page = await browser.newPage();
  console.log("new page created")

  await page.setViewport({
    width: 1920,
    height: 1080
  });

  return new Test(browser, page);
}

class Test {

  constructor(browser, page) {
    this.browser = browser;
    this.page = page;
  }

  getBrowser() {
    return this.browser;
  }

  async screenshot(name) {
    await this.page.screenshot(
        {path: '/testoutput/' + name + '.png', fullPage: true});
    console.log("screenshot created");
  }

  async goto(path) {
    await this.page.goto(this.getUrl() + path, {
      waitUntil: 'networkidle0',
    });

    console.log("page navigated");
  }

  async waitForNetworkIdle() {
    await this.page.waitForNavigation({
      waitUntil: 'networkidle0',
    });
  }

  async teardown() {
    await this.browser.close();
    console.log("browser closed")
  }

  getPage() {
    return this.page;
  }

  async executeGql(gqlQuery) {
    // Setting URL and headers for request
    const options = {
      url: this.getUrl() + "/graphql",
      json: {
        query: gqlQuery
      }
    };

    // Return new promise
    return new Promise(function(resolve, reject) {
      // Do async job
      console.log("execute gql:", gqlQuery)
      request.post(options, function(err, resp, body) {
        if (err) {
          console.log("Error during gql execution:", err)
          reject(err);
        } else {
          console.log("Response received:", body)
          resolve(body);
        }
      })
    });
  }

  getUrl() {
    return "http://alarmcontrol:8080";
  }
}

module.exports = {
  setup
};