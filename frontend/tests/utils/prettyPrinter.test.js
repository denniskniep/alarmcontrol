import {prettifyDateLong,prettifyDateTimeLong,prettifyDateTimeShort} from "../../src/utils/prettyPrinter";

const date = new Date(2019,7,19,20,15,2, 123);

test('print date long', () => {
  let formatted = prettifyDateLong(date);
  expect(formatted).toBe("19.08.2019");
});

test('print datetime long', () => {
  let formatted = prettifyDateTimeLong(date);
  expect(formatted).toBe("19.08.2019 20:15:02");
});

test('print datetime short', () => {
  let formatted = prettifyDateTimeShort(date);
  expect(formatted).toBe("19.08.2019 20:15");
});