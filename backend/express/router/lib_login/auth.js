var express = require('express');
var router = express.Router();
var db = require('../../db');

// 로그인 화면
router.get('/login', function (request, response) {
    var title = '로그인화면';
    response.json({ "title": title })
});

// 로그인 프로세스
router.post('/login', (req, res) => {
    var userId = req.body.userId;
    var password = req.body.password;
    var errorcode = true;

    if (userId && password) {
        db.connect();
        db.query('SELECT * FROM user WHERE userId = ?', [userId], function (error, results, fields) {
            if (error) throw error;
            if (results.length > 0) {       // db에서의 반환값이 있으면 로그인 성공
                if (password == results[0].password) { // 회원정보의 password와 입력한 password가 같은 경우
                    req.session.is_logined = true;
                    req.session.nickname = userId;
                    // req.session.save(function () {
                    //     res.redirect(`/`);
                    // });
                    errorcode = false;
                    res.json({
                        "error": errorcode,
                        "userId": userId,
                        "result": "Login Success!"
                    })
                } else { // 입력한 비밀번호가 다를경우
                    res.json({
                        "error": errorcode,
                        "userId": userId,
                        "result": "비밀번호가 틀렸습니다."
                    })
                }
            } else { //입력한 아이디에 대한 정보가 없을때
                res.json({
                    "error": errorcode,
                    "userId": userId,
                    "result": "존재하지 않는 회원입니다."
                })
            }
        });
        db.end();
    } else {
        res.json({
            "error": errorcode,
            "message": "아이디 및 비밀번호를 입력하세요."
        })
    }
});

// 로그아웃
router.get('/logout', function (request, response) {
    request.session.destroy(function (err) {
        response.json({ "result": "main으로 돌아갑니다" })
    });
});

// 회원가입 화면
router.get('/register', function (req, res) {
    var title = '회원가입 화면';
    res.json({ "title": title })
});

// 아이디 중복 확인 프로세스
router.post('/id-check', (req, res) => {
    var userId = req.body.userId;
    var errorcode = true;

    db.connect();
    db.query('select * from user where userId = ?', [userId], function (error, results, fields) {
        if (error) throw error;

        if (results.length <= 0) {  //중복되는 아이디가 없음
            errorcode = false;
            res.json({
                "error": errorcode,
                "message": "사용가능한 아이디 입니다."
            })
        } else {           // DB에 같은 이름의 회원아이디가 있는 경우
            res.json({
                "error": errorcode,
                "message": "이미 존재하는 아이디 입니다."
            })
        }
    });
    db.end();
})

//닉네임 중복 확인 프로세스
router.post('/nickname-check', (req, res) => {
    var nickname = req.body.nickname;
    var errorcode = true;

    db.connect();
    db.query('select * from user where nickname = ?', [nickname], function (error, results, fields) {
        if (error) throw error;

        if (results.length <= 0) {  //중복되는 닉네임이 없음
            errorcode = false;
            res.json({
                "error": errorcode,
                "message": "사용가능한 닉네임 입니다."
            })
        } else {           // 중복되는 닉네임이 있는 경우
            res.json({
                "error": errorcode,
                "message": "이미 존재하는 닉네임 입니다."
            })
        }
    });
    db.end();
})

// 회원가입 프로세스
router.post('/register', (req, res) => {
    var userId = req.body.userId;
    var username = req.body.username;
    var phone = req.body.phone;
    var password = req.body.password;
    var password2 = req.body.password2;
    var nickname = req.body.nickname;

    var errorcode = true;

    if (username && phone && userId && password && password2 && nickname) {
        if (password == password2) { // 비밀번호가 올바르게 입력된 경우 
            db.connect();
            db.query('INSERT INTO user (userId, username, phone, password, nickname) VALUES(?,?,?,?,?)', [userId, username, phone, password, nickname], function (error, data) {
                if (error) throw error;
                errorcode = false
                res.json({
                    "error": errorcode,
                    "username": username,
                    "password": password
                })
            })
            db.end();
        } else {                     // 비밀번호가 올바르게 입력되지 않은 경우
            res.json({
                "error": errorcode,
                "message": "입력한 비밀번호 정보가 다릅니다."
            })
        }
    } else {        // 입력되지 않은 정보가 있는 경우
        res.json({
            "error": errorcode,
            "result": "입력되지 않은 정보가 있습니다"
        })
    }
});

module.exports = router;