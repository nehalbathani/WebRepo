import React, {Component} from 'react';
import './Dropdown.css';

class DropdownNumber extends Component {
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
  const htmltxt = Object.entries(dropdowndata).map(([start,end])=>{
  return (
    var rows = [];
    for (var i = start; i < end; i--) {
      // note: we add a key prop here to allow react to uniquely identify each
      // element in this array. see: https://reactjs.org/docs/lists-and-keys.html
      rows.push(<option key={i} value={i}> {i} </option>);
    }

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
