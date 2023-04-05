import React, {useState} from 'react';

export default function Login({setToken}) {

    const [username, setUserName] = useState();
    const [password, setPassword] = useState();

    const handleSubmit = async e => {
        e.preventDefault();
        const tokenRaw = await fetch('http://localhost:8080/authentication', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                "login": username,
                "password": password
            })
        }).then(data => data.json())
        const token = JSON.stringify(tokenRaw);
        if (token.includes("Bearer")) {
            setToken(tokenRaw.data);
        }
    }

    return (
        <div className="container">
            <div className="login-wrapper">
                <form className="row" onSubmit={handleSubmit}>
                    <div className="mb-3">
                        <label htmlFor="inputLogin" className="form-label">Login</label>
                        <input type="text" className="form-control" id="inputLogin"
                               onChange={e => setUserName(e.target.value)}/>
                    </div>
                    <div className="mb-3">
                        <label htmlFor="inputPassword" className="form-label">Password</label>
                        <input type="password" className="form-control" id="inputPassword"
                               onChange={e => setPassword(e.target.value)}/>
                    </div>
                    <button type="submit" className="btn btn-primary">Submit</button>
                </form>
            </div>
        </div>
    )
}
