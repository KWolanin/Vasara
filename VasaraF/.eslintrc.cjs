module.exports = {
  root: true,
  parser: "vue-eslint-parser",
  parserOptions: {
    parser: "@typescript-eslint/parser",
    ecmaVersion: 2020,
    sourceType: "module",
    ecmaFeatures: {
      jsx: true,
    },
    env: {
      node: true,
      browser: true,
    },

    extends: [
      "eslint:recommended",
      "plugin:vue/vue3-essential",
      "prettier",
      "plugin:@typescript-eslint/recommended",
    ],

    plugins: ["vue", "@typescript-eslint"],

    globals: {
      ga: "readonly", // Google Analytics
      cordova: "readonly",
      __statics: "readonly",
      __QUASAR_SSR__: "readonly",
      __QUASAR_SSR_SERVER__: "readonly",
      __QUASAR_SSR_CLIENT__: "readonly",
      __QUASAR_SSR_PWA__: "readonly",
      process: "readonly",
      Capacitor: "readonly",
      chrome: "readonly",
    },

    // add your custom rules here
    rules: {
      "prefer-promise-reject-errors": "off",

      // allow debugger during development only
      "no-debugger": process.env.NODE_ENV === "production" ? "error" : "off",
    },
  },
};
