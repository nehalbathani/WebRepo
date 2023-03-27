import React, {Component} from 'react';
import './NewPlayer.css';
import Input from '../../../layout/UI/Input';

const NewPlayer = () => {

    return (
      <div className="NewPlayer" >
        <form  action="/action_page.php">
          <div >
            <h2>Registration </h2>
            <hr/>
            <Input lbltext='First Name' inputtype='text'/>
            <Input lbltext='Last Name' inputtype='text'/>
            <Input lbltext='Age' inputtype='text'/>
            <Input lbltext='Street Address:' inputtype='text'/>
            <Input lbltext='City' inputtype='text'/>
            <Input lbltext='State' inputtype='text'/>
            <Input lbltext='Zipcode' inputtype='text'/>
            <Input lbltext='CricClub ID' inputtype='text'/>
            <div style={{display:'inline-block', position:'absolute'}}>
            <Input lbltext='Player Type' inputtype='label'/>
            <Input lbltext='' inputtype='radio' groupname='player_type' val='Regular' radiotxt='Regular player (40$)'/>
            <Input lbltext='' inputtype='radio' groupname='player_type' val='Guest' radiotxt='Guest player (10$ - maximum 3 games per year)'/>
            </div>
            <div style={{display:'inline-block', margin:'0px 550px'}}>
            <Input lbltext='Payment Method' inputtype='label'/>
            <Input lbltext='' inputtype='imgradio' groupname='pay_method' val='BOA' imgfilename='SiteLogo.JPG'/>
            <Input lbltext='' inputtype='imgradio' groupname='pay_method' val='Chase' imgsrc=''/>
            <Input lbltext='' inputtype='imgradio' groupname='pay_method' val='PayPal' imgsrc=''/>
            </div>
            <p>By creating an account you agree to our <a href="#" style={{color:'dodgerblue'}}>Terms & Privacy</a>.</p>

            <div className="clearfix">
              <button type="button" className="cancelbtn">Cancel</button>
              <button type="button" className="signup">Sign Up</button>
            </div>
          </div>
        </form>
      </div>
    );

}

export default NewPlayer;
