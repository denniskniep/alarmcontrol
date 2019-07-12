import React, {Component} from 'react';
import {CurrentOrganisationContext} from "./currentOrganisationContext";

const ALL = 0;
const ORGANISATION_ID_KEY = 'organisationId';

class CurrentOrganisationContainer extends Component {

  constructor(props) {
    super(props);
    this.state = {
      organisationId: this.getCurrentOrganisationId()
    }
  }

  getCurrentOrganisationId() {
    let organisationId = localStorage.getItem(ORGANISATION_ID_KEY);
    if (!organisationId) {
      return ALL;
    }
    return organisationId;
  }

  setCurrentOrganisationId(organisationId) {
    localStorage.setItem(ORGANISATION_ID_KEY, organisationId);
    this.setState({ organisationId: organisationId})
  }

  render(){
    return(
        <CurrentOrganisationContext.Provider value={{
          setCurrentOrganisationId: (organisationId) => this.setCurrentOrganisationId(organisationId),
          organisationId: this.state.organisationId
        }}>
          {this.props.children}
        </CurrentOrganisationContext.Provider>
    )
  }
}

export default CurrentOrganisationContainer