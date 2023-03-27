import React, {Component} from 'react';
import './Dropdown.css';

class Dropdown extends Component {
state={
  optiontxt:''
}
/*
componentDidMount () {
  console.log('reached');
  this.setState({optiontxt:<option value="sample">test</option>});
  console.log(this.state.optiontxt);
}
*/
render(){
  let styles = {width: '95%', margin: '5px'};
  let dropdowndata = this.props.data;
  const htmltxt = Object.entries(dropdowndata).map(([key,value])=>{
  return (
      <option key={key} value={key}> {value.toString()} </option>
  );
})
    return (
      <select id={this.props.id} name={this.props.selname} style={styles} className="form-control Dropdown" onChange={this.props.onChange}>
      {htmltxt}
      </select>
    );
}
}

export default Dropdown;
