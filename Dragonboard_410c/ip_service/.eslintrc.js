module.exports = {
    "parserOptions": {
      "ecmaVersion": "2017",
      "sourceType": "module"
    },
    "env": {
      "node": true,
      "es6": true
    },
    "extends": "eslint:recommended",
    "rules": {
      "no-console" : 0,
      "no-multiple-empty-lines": "warn",
      "no-var": "error",
      "prefer-const": "error"
    }
  };