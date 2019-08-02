(async function() {
  const testSetup = require('./lib/testSetup');
  const test = await testSetup.setup();
  await test.goto('/');
  await test.executeGql('mutation { \n'
      + '  newAlertCall(\n'
      + '    organisationId: 1\n'
      + '    alertNumber: "123456-S04"\n'
      + '    alertReferenceId: "B123456"\n'
      + '    alertCallReferenceId: "123"\n'
      + '    keyword: "H1"\n'
      + '    address:"Hinter den Gärten 8, 34379 Calden"\n'
      + '    # dateTime: "2019-05-03T12:23:32.456Z"\n'
      + '  ){\n'
      + '    id\n'
      + '    alert {\n'
      + '      id\n'
      + '    }\n'
      + '  }\n'
      + '}\n');

  //Wait for header
  await test.getPage().waitFor('h1[data-testid="keyword"]');

  await test.getPage().waitFor(3000);

  await test.screenshot("002_onHomepage_NewAlert_alert_001");
  await test.teardown();
})();