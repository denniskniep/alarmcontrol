(async function() {
  const testSetup = require('./lib/testSetup');
  const test = await testSetup.setup();
  await test.goto('/');
  await test.screenshot("001_displayHomepage_homepage");
  await test.teardown();
})();