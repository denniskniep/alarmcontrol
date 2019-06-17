import React, {Component} from 'react';
import { Nav, Navbar} from "react-bootstrap";
import {NavLink} from "react-router-dom";
import NavDropdown from "react-bootstrap/NavDropdown";
import {gql} from "apollo-boost";
import {Query} from "react-apollo";

const ALERTS_BY_ORGANISATION = gql`
  query alertsByOrganisationId($id: ID) {
    alertsByOrganisationId(organisationId: $id) {
      id,
      keyword,
      dateTime
    }
  }
`;

class Menu extends Component {
  render() {
    return (
        <Navbar sticky="top" collapseOnSelect expand="lg" bg="dark" variant="dark">
          <Navbar.Brand>AlarmControl</Navbar.Brand>
          <Navbar.Toggle aria-controls="responsive-navbar-nav" />
          <Navbar.Collapse id="responsive-navbar-nav">
            <Nav className="mr-auto">
              <NavLink className={"nav-link"} exact to="/">Home</NavLink>
              <NavDropdown title="Manage">
                <NavDropdown.Item href="/manage/organisation">Organisations</NavDropdown.Item>
              </NavDropdown>
              <NavDropdown title="Alerts">
                <Query query={ALERTS_BY_ORGANISATION} variables={{id: 1}}>
                  {({loading, error, data}) => {
                    if (loading || error || !data.alertsByOrganisationId) {
                      return <p></p>;
                    }

                    return data.alertsByOrganisationId.map((value, index) => {
                        return <NavDropdown.Item href={"/alertview/" + value.id}>{value.keyword}</NavDropdown.Item>
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