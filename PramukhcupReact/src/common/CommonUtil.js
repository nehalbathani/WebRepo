const createReactClass = require('create-react-class');

const CommonUtil = createReactClass({
  statics: {
    getURL: function(){
      if(window.location.hostname=='localhost'){
        return "http://ec2-54-165-35-254.compute-1.amazonaws.com:8080";
      } else {
        return "";
      }
    },
  },
  render() {
    return;
  },
});

export default CommonUtil;
