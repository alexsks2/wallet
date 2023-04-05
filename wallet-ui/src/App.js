import React, {useState} from 'react';
import '../node_modules/bootstrap/dist/css/bootstrap.min.css';
import "bootstrap-icons/font/bootstrap-icons.css";
import WalletList from "./components/wallet/WalletList";
import {connect} from "react-redux";
import Login from "./components/authentication/Login";

function App() {

    const [token, setToken] = useState();

    const render = () => {
        if (!token) {
            return <Login setToken={setToken}/>
        } else {
            return <WalletList setToken={setToken} token={token}/>
        }
    }

    return (
        <div className="App">
            <div className="container-sm">
                <div className="row">
                    <div className="col">
                        {render()}
                    </div>
                </div>
            </div>
        </div>
    );
}

export default connect()(App);
