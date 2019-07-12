import React, {Component} from 'react';
import {Nav, Navbar} from "react-bootstrap";
import {NavLink} from "react-router-dom";
import NavDropdown from "react-bootstrap/NavDropdown";
import {gql} from "apollo-boost";
import {Query} from "react-apollo";
import PrettyPrinter from "./utils/prettyPrinter";
import QueryDefaultHandler from "./utils/queryDefaultHandler";
import Form from "react-bootstrap/Form";
import AlertAddedSubscription from "./alertview/alertAddedSubscription";
import {CurrentOrganisationContext} from "./currentOrganisationContext";

const ALERTS_BY_ORGANISATION = gql`
  query alertsByOrganisationId($organisationId: ID, $page: Int!, $size: Int!) {
    alertsByOrganisationId(organisationId: $organisationId, page: $page, size: $size) {
      id,
      keyword,
      dateTime,
      addressInfo1
    }
  }
`;

const ORGANISATIONS = gql`
  query organisations {
    organisations {
      id,
      name
    }
  }
`;

class Menu extends Component {

  constructor(props) {
    super(props);
  }

  handleOrganisationChanged(changed, refetch, organisationContext) {
    organisationContext.setCurrentOrganisationId(changed.target.value);
    refetch();
  }

  getValidOrganisationId(organisationId, organisations) {
    if (organisationId != 0 && organisations.filter(
        o => o.id == organisationId).length == 0) {
      return null;
    }
    return organisationId == 0 ? null : organisationId * 1;
  }

  render() {
    return (

        <CurrentOrganisationContext.Consumer>
          { organisationContext => {
            return (

                <Query query={ORGANISATIONS}
                       fetchPolicy="no-cache">
                  {({loading, error, data}) => {

                    let result = new QueryDefaultHandler().handleGraphQlQuery(
                        loading,
                        error,
                        data,
                        data && data.organisations);

                    if (result) {
                      return result;
                    }

                    let organisations = data.organisations;

                    return (
                        <Query query={ALERTS_BY_ORGANISATION}
                               fetchPolicy="no-cache"
                               variables={{
                                 organisationId: this.getValidOrganisationId(organisationContext.organisationId, organisations),
                                 page: 0,
                                 size: 10
                               }}>
                          {({loading, error, data, refetch}) => {
                            let result = new QueryDefaultHandler().handleGraphQlQuery(
                                loading,
                                error,
                                data,
                                data && data.alertsByOrganisationId);

                            if (result) {
                              return result;
                            }

                            return (
                                <React.Fragment>
                                  <AlertAddedSubscription
                                      onSubscriptionData={o => refetch()}/>
                                  <Navbar sticky="top" collapseOnSelect
                                          expand="lg"
                                          bg="dark"
                                          variant="dark">
                                    <Navbar.Brand>AlarmControl</Navbar.Brand>
                                    <Navbar.Toggle
                                        aria-controls="responsive-navbar-nav"/>
                                    <Navbar.Collapse id="responsive-navbar-nav">
                                      <Nav className="mr-auto">
                                        <NavLink className={"nav-link"} exact
                                                 to="/app/">Home</NavLink>


                                        <React.Fragment>
                                          <NavDropdown title="Last Alerts">
                                            {
                                              data.alertsByOrganisationId.map(
                                                  (alert, index) => {
                                                    return <NavDropdown.Item
                                                        key={alert.id}
                                                        href={"/app/alertview/"
                                                        + alert.id}>
                                                      {
                                                        alert.keyword +
                                                        (alert.addressInfo1
                                                            ? " - "
                                                            + alert.addressInfo1
                                                            : "") +
                                                        " (" +
                                                        new PrettyPrinter().prettifyDateTimeLong(
                                                            alert.dateTime) +
                                                        ")"
                                                      }
                                                    </NavDropdown.Item>
                                                  })
                                            }
                                          </NavDropdown>
                                        </React.Fragment>


                                      </Nav>
                                      <Nav>
                                        <NavDropdown title="Manage">
                                          <NavDropdown.Item
                                              href="/app/manage/organisation">Organisations</NavDropdown.Item>
                                        </NavDropdown>

                                        <Form inline>
                                          <Form.Group controlId="formGridState">
                                            <Form.Label
                                                className={"navLabel"}>Organisation:</Form.Label>
                                            <Form.Control as="select"
                                                          value={organisationContext.organisationId}
                                                          onChange={
                                                            c => this.handleOrganisationChanged(
                                                                c, refetch,
                                                                organisationContext)}>
                                              <option value={0}>All</option>
                                              {
                                                organisations.map(
                                                    (organisation, index) => {
                                                      return (
                                                          <option
                                                              key={organisation.id}
                                                              value={organisation.id}
                                                          >
                                                            {organisation.name}
                                                          </option>)
                                                    })
                                              }
                                            </Form.Control>
                                          </Form.Group>
                                        </Form>

                                      </Nav>
                                    </Navbar.Collapse>
                                  </Navbar>
                                </React.Fragment>
                            )
                          }}
                        </Query>
                    )
                  }}
                </Query>

            );
          }}
        </CurrentOrganisationContext.Consumer>
    );
  }
}

export default Menu