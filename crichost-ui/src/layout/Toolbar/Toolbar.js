import React, {Component} from 'react';
import './Toolbar.css';
import Logo from '../Logo/Logo'

const Toolbar = () => {

    return (
      <div className="Toolbar">
          <span> Avenel Premier League - 2018</span>
          <Logo/>
      </div>
    );

}

export default Toolbar;
