import React from 'react';
import Mutation from "react-apollo/Mutation";
import {toast} from "react-toastify";

function errorHandler(error) {
  let details = "Error during operation!";
  if(error && error.message) {
    details = error.message;
  }

  toast.error(details, {
    position: "top-right",
    autoClose: 3000,
    hideProgressBar: false,
    closeOnClick: true,
    pauseOnHover: true,
    draggable: true
  });
}

function MutationHandler(props) {
  const {children, ...restProps} = props;
  return <Mutation {...restProps}
                   onError={(error) => errorHandler(error)}>
    {(execute) => {
      return children(execute);
    }}
  </Mutation>
}

export default MutationHandler;