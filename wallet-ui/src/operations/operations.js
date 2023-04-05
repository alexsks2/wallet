import {initCards} from "../redux/cardActions";

const fetchTemplate = (url, content, props, dispatch) => {

    const fetchData = async () => {

        fetch(url, content)
            .then(response => {
                if (response.ok) {
                    getWallets(props, dispatch);
                    props.setError(null);
                } else {
                    return Promise.reject(response);
                }
            }).catch(error => {
            console.log("!@#", error)
            error.json().then((json: any) => {
                console.log(json);
                props.setError(json.errorMessage);
            })
        });

    }
    fetchData();
}

export const getWallets = (props, dispatch) => {

    const fetchData = async () => {
        const data = await fetch('http://localhost:8080/wallet', {
            method: 'GET',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json',
                'Authorization': props.token,
            }
        });
        const json = await data.json();
        dispatch(initCards(json.data))
    }
    fetchData().catch(console.error)
}

export const addNewCard = (props, dispatch, cardName, currency) => {

    fetchTemplate('http://localhost:8080/wallet', {
        method: 'POST',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
            'Authorization': props.token,
        },
        body: JSON.stringify({
            walletName: cardName,
            currencyType: currency
        })
    }, props, dispatch);
}

export const cashIn = (props, dispatch, amount) => {


    fetchTemplate(`http://localhost:8080/wallet/${props.value.id}/top-up`, {
        method: 'PATCH',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
            'Authorization': props.token,
        },
        body: JSON.stringify({amount})
    }, props, dispatch);
}

export const cashOut = (props, dispatch, amount) => {

    fetchTemplate(`http://localhost:8080/wallet/${props.value.id}/withdraw`, {
        method: 'PATCH',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
            'Authorization': props.token,
        },
        body: JSON.stringify({amount})
    }, props, dispatch);
}

export const transfer = (props, dispatch, amount, recipientId) => {

    fetchTemplate(`http://localhost:8080/wallet/${props.value.id}/transfer`, {
        method: 'PATCH',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
            'Authorization': props.token,
        },
        body: JSON.stringify({amount, walletId: recipientId})
    }, props, dispatch);
}