const express = require('express')
const session = require('express-session')
const bodyParser = require('body-parser');
const FileStore = require('session-file-store')(session)
const app = express()

var authRouter = require('../lib_login/auth');
var authCheck = require('../lib_login/authCheck.js');

app.use('/static', express.static('static'))
app.use(express.urlencoded({
    extended: true
}))
app.use(express.json());
const port = 3000

app.use(bodyParser.json())
app.use(bodyParser.urlencoded({ extended: false }));
app.use(session({
    secret: 'secret test',	// 원하는 문자 입력
    resave: false,
    saveUninitialized: true,
    store: new FileStore(),
}))

app.post('/test', (req, res) => {
    console.log(req.body);
    const result = req.body;
    res.send(result);
})

app.get('/', (req, res) => {
    // if (!authCheck.isOwner(req, res)) {  // 로그인 안되어있으면 로그인 페이지로 이동시킴
    //     res.json({ "result": "Login page로 이동" })
    // } else {                                      // 로그인 되어있으면 메인 페이지로 이동시킴
    //     res.json({ "result": "main으로 이동" })
    // }
    res.json('Hello World');
})

// 인증 라우터
app.use('/auth', authRouter);

// 메인 페이지
app.get('/main', (req, res) => {
    if (!authCheck.isOwner(req, res)) {  // 로그인 안되어있으면 로그인 페이지로 이동시킴
        res.json({ "result": "Login page로 이동" })
    }
    res.json({ "result": "Login Success! 메인화면입니다" })
})

app.listen(port, () => {
    console.log(`Example app listening on port ${port}`)
})