import ProdConfiguration from "./prodConfiguration";
import DevConfiguration from "./devConfiguration";

const configuration = process.env.NODE_ENV === 'production' ? new ProdConfiguration() : new DevConfiguration();
export default configuration;