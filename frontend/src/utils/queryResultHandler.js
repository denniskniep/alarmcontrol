import React, {useEffect} from 'react';
import {toast} from "react-toastify";

function QueryResultHandler(props) {
  const {loading, error, data, children} = props;

  // Hook:
  // Do not execute on every component render
  // Execute only if inputs array change
  useEffect(() => {
    if (error) {
      let details = "Error during query:";
      if(error && error.message) {
        details = error.message;
      }

      console.error(error);
      toast.error(details, {
        position: "top-right",
        autoClose: 3000,
        hideProgressBar: false,
        closeOnClick: true,
        pauseOnHover: true,
        draggable: true
      });
    }
  }, [error && error.message]);

  if (loading) {
    return <p>Loading...</p>;
  }

  if (error) {
   return <React.Fragment></React.Fragment>;
  }

  if (!data) {
    return <React.Fragment></React.Fragment>;
  }

  return children(props);
}

export default QueryResultHandler;