import React, {useEffect, useState} from "react";
import Wallet from "./Wallet";
import AddNewCard from "../operations/NewCardModal";
import {connect, useDispatch, useSelector} from "react-redux";
import store from "../../redux/store";
import {getWallets} from "../../operations/operations";

function WalletList(props) {

    const cardSelector = useSelector(state => state.value);
    const dispatch = useDispatch();
    const [showAddCard, setShowAddCard] = useState(true);
    const [error, setError] = useState();

    useEffect(() => {
        getWallets(props, dispatch);
    }, [cardSelector, dispatch]);

    function renderError() {
        if (error) {
            return <p>{error}</p>
        }
    }

    return (
        <div className="container">
            {renderError()}
            <div className="d-flex align-content-around flex-wrap">
                {store.getState().map(value => {
                    return <div key={value.id} className="p-2">
                        <Wallet token={props.token} key={value.id} value={value} setError={setError}/>
                    </div>
                })}
            </div>
            <i className="bi bi-file-plus" onClick={() => setShowAddCard(!showAddCard)}></i>
            <AddNewCard token={props.token} hiden={showAddCard} setShowAddCard={setShowAddCard} setError={setError}/>
        </div>
    )
}

function mapStateToProps(state) {
    return {
        cards: state
    };
}

export default connect(mapStateToProps)(WalletList);