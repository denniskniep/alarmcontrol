import React from 'react';
import {Query} from "react-apollo";
import QueryResultHandler from "./queryResultHandler";

function QueryHandler(props) {
  const {children, ...restProps} = props;
  return <Query {...restProps}>
    {(queryResults) => {
      return (<QueryResultHandler {...queryResults}>
        {(queryResults) => {
          return children(queryResults);
        }}
      </QueryResultHandler>)
    }}
  </Query>
}

export default QueryHandler;