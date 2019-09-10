// import dependencies
import React from 'react'

// import react-testing methods
import {
  render,
  cleanup,
} from '@testing-library/react'

// add custom jest matchers from jest-dom
import '@testing-library/jest-dom/extend-expect'

// the component to test
import AlertViewHeader from '../../../src/alertview/tiles/alertViewHeader'

// automatically unmount and cleanup DOM after the test is finished.
afterEach(cleanup)

test('loads and displays alert view header', async () => {
  // Arrange
  const alert = {
    keyword: "BF1",
    distance: 3595.037,
    duration: 178451,
    dateTime: "2019-07-18T21:49:06.495Z",
    addressInfo1: "Wilhelmsthaler Straße 12",
    addressInfo2: "Calden",
    addressRaw: "CALDEN WILHELMSTHALER 12 CALDEN",
    alertCalls: [{
      id: "1",
      alertNumber: {
        number: "123456-S04",
        shortDescription: "Pager"
      }
    },
      {
        id: "2",
        alertNumber: {
          number: "123456-S54",
          shortDescription: "Sirene"
        }
      }],
    description: null
  }

  const {getByText, getByTestId, container, asFragment} = render(
      <AlertViewHeader alert={alert}/>
  )

  // Act
  // There is a fireEvent method that allows you to simulate user actions.

  // Assert
  expect(getByTestId('keyword')).toHaveTextContent('BF1')
  expect(getByTestId('address1Header')).toHaveTextContent(
      'Wilhelmsthaler Straße 12')
  expect(getByTestId('address2Header')).toHaveTextContent('Calden')
  expect(getByTestId('description')).toBeEmpty()
  expect(getByTestId('date')).toHaveTextContent('18.07.2019 23:49:06')
  expect(getByTestId('routeInfo')).toHaveTextContent('3.6 km (3 min)')
  expect(getByTestId('alertCalls')).toHaveTextContent('Pager')
  expect(getByTestId('alertCalls')).toHaveTextContent('Sirene')
})