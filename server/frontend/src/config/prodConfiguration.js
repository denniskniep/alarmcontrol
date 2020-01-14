class ProdConfiguration {

  getServer() {
    let portPostfix = window.location.port ? ":" + window.location.port : "";
    return window.location.hostname + portPostfix;
  }
}

export default ProdConfiguration;