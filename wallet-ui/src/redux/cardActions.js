const initCards = (cards) => {
    return (dispatch) => {
        dispatch({type: 'INIT_CARDS', cards: [...cards]})
    }
}
export {
    initCards
}