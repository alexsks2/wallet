import React, {useState} from "react";
import {useDispatch} from "react-redux";
import {addNewCard} from "../../operations/operations";


function NewCardModal(props) {

    const dispatch = useDispatch();
    const [cardName, setCardName] = useState('')
    const [currency, setCurrency] = useState('EUR')

    function addNewCardOperation() {
        addNewCard(props, dispatch, cardName, currency);
        props.setShowAddCard(!props.hiden);
    }

    return (
        <div hidden={props.hiden}>
            <div className="card text-dark bg-light mb-4 card-size ">

                <div className="card-header">

                    <div className="form-floating">
                        <textarea className="form-control" placeholder="Enter card name"
                                  onChange={(event) => setCardName(event.target.value)}></textarea>
                        <label htmlFor="floatingTextarea">Card name</label>

                    </div>
                </div>

                <div className="card-body">
                    <div className="row">
                        <div className="col">

                            <select onChange={(event) => setCurrency(event.target.value)}>
                                <option>EUR</option>
                            </select>
                        </div>
                        <div className="col">
                            <button type="button" onClick={addNewCardOperation}>Add new card</button>
                        </div>
                    </div>
                </div>
            </div>

        </div>
    )
}

export default NewCardModal;