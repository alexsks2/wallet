import React, {useState} from "react";
import {Button, Modal} from "react-bootstrap";
import {connect, useDispatch} from "react-redux";
import {cashIn, cashOut, transfer} from "../../operations/operations";

function Wallet(props) {

    const dispatch = useDispatch();

    const [show, setShow] = useState(false);
    const [amount, setAmount] = useState('');
    const [transferRecipientId, setTransferRecipientId] = useState(props.cards[0].id);

    const handleChangeRecipient = (target) => {
        setTransferRecipientId(target.value);
    }

    const cashInOperation = () => {
        cashIn(props, dispatch, amount);
        setShow(!show);
        setAmount(null);
    }

    const cashOutOperation = () => {
        cashOut(props, dispatch, amount);
        setShow(!show)
        setAmount(null);
    }

    const transferOperation = () => {
        transfer(props, dispatch, amount, transferRecipientId);
        setShow(!show)
        setAmount(null);
    }

    return (
        <div className="col">

            <div className={"card text-white  mb-4 card-size bg-primary"} onClick={() => setShow(!show)}>
                <div className="card-header">{props.value.name ? props.value.name : 'Name'}</div>
                <div className="card-body">
                    <h5 className="card-title">
                        {`${props.value.balance}  ${props.value.currency}`}
                    </h5>
                    <span className="card-text">
                        <div>
                            <i className="bi-arrow-down-circle">Operations</i>
                        </div>
                    </span>
                </div>
            </div>

            <Modal show={show} onHide={() => setShow(!show)}>

                <Modal.Header closeButton>
                    <Modal.Title>Card operations</Modal.Title>
                </Modal.Header>

                <Modal.Body>
                    Pls choose the operation for card <strong>{props.value.name}</strong>,
                    balance <strong>{props.value.balance}</strong> {props.value.currency}
                </Modal.Body>

                <Modal.Footer>
                    <div className="input-group mb-3">
                        <div className="input-group mb-3">
                            <span className="input-group-text">$</span>
                            <span className="input-group-text">0.00</span>
                            <input type="number" className="form-control"
                                   aria-label="Dollar amount (with dot and two decimal places)" value={amount}
                                   onChange={(event) => setAmount(event.target.value)}/>
                        </div>

                        <Button variant="primary" onClick={() => cashInOperation()}>cash-in</Button>
                        <Button variant="primary" onClick={() => cashOutOperation()}>cash-out</Button>

                        <select className="form-select" onChange={(event) => handleChangeRecipient(event.target)}>
                            {props.cards.map(card => {
                                return <option id={card.id} key={card.id} value={card.id}>{card.name}</option>
                            })}
                        </select>
                        <button className="btn btn-primary" type="button" onClick={() => transferOperation()}>
                            Transfer
                        </button>
                    </div>

                </Modal.Footer>

            </Modal>
        </div>
    )
}

function mapStateToProps(state) {
    return {
        cards: state
    };
}

export default connect(mapStateToProps)(Wallet);