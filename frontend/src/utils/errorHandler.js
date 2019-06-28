import {toast} from "react-toastify";

class ErrorHandler {

  handleGraphQlMutationError(error) {
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

}

export default ErrorHandler;