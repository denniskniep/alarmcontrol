import React, {Component} from 'react';
import {Nav, Navbar} from "react-bootstrap";
import {NavLink} from "react-router-dom";
import NavDropdown from "react-bootstrap/NavDropdown";
import {gql} from "apollo-boost";
import {Query} from "react-apollo";
import PrettyPrinter from "./utils/prettyPrinter";

const ALERTS_BY_ORGANISATION = gql`
  query alertsByOrganisationId($id: ID, $page: Int, $size: Int) {
    alertsByOrganisationId(organisationId: $id, page: $page, size: $size) {
      id,
      keyword,
      dateTime,
      addressInfo1
    }
  }
`;

class Menu extends Component {
  render() {
    return (
        <Navbar sticky="top" collapseOnSelect expand="lg" bg="dark"
                variant="dark">
          <Navbar.Brand>AlarmControl</Navbar.Brand>
          <Navbar.Toggle aria-controls="responsive-navbar-nav"/>
          <Navbar.Collapse id="responsive-navbar-nav">
            <Nav className="mr-auto">
              <NavLink className={"nav-link"} exact to="/app/">Home</NavLink>
              <NavDropdown title="Manage">
                <NavDropdown.Item
                    href="/app/manage/organisation">Organisations</NavDropdown.Item>
              </NavDropdown>
              <NavDropdown title="Last Alerts">
                <Query query={ALERTS_BY_ORGANISATION}
                       fetchPolicy="no-cache"
                       variables={{id: 1, page: 0, size: 10}}>
                  {({loading, error, data}) => {
                    if (loading || error || !data.alertsByOrganisationId) {
                      return <p></p>;
                    }

                    return data.alertsByOrganisationId.map((alert, index) => {
                      return <NavDropdown.Item key={alert.id}
                                               href={"/app/alertview/" + alert.id}>
                        {
                          alert.keyword +
                          (alert.addressInfo1 ? " - " + alert.addressInfo1 : "") +
                          " (" +
                          new PrettyPrinter().prettifyDateTimeLong(alert.dateTime) +
                          ")"
                        }
                      </NavDropdown.Item>
                    });
                  }}
                </Query>

              </NavDropdown>
            </Nav>
          </Navbar.Collapse>
        </Navbar>
    );
  }
}

export default Menu