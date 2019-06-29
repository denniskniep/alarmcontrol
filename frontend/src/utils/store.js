const ALL = 0;
const ORGANISATION_ID_KEY = 'organisationId';

class Store {

  getCurrentOrganisationId() {
    let organisationId = localStorage.getItem(ORGANISATION_ID_KEY);
    if (!organisationId) {
      return ALL;
    }
    return organisationId;
  }

  setCurrentOrganisationId(organisationId) {
    localStorage.setItem(ORGANISATION_ID_KEY, organisationId);
  }

}

const store = new Store();
export default store;