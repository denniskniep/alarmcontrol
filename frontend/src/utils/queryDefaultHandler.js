import React from 'react';

class QueryDefaultHandler {

  handleGraphQlQuery(loading, error, data, object) {
    if (loading) {
      return <p>Loading...</p>;
    }

    if (error) {
      return <p>Error: ${error.message}</p>;
    }

    if (!object) {
      return <p>NO DATA!!!!</p>;
    }

    return null;
  }

}

export default QueryDefaultHandler;