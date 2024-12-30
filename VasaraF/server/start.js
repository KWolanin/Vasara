// this script comes from: https://github.com/railwayapp-templates/vue-starter
// it allows to deploy the Quasar app to Railway

const express = require("express"),
  serveStatic = require("serve-static"),
  port = process.env.PORT || 3000;

const app = express();

app.use(serveStatic("../dist/spa", { index: ["index.html", "index.htm"] }));
app.listen(port);
