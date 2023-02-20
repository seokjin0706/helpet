var express = require('express');
var router = express.Router();
var db = require('../../db');

// 달력화면
router.get('/', function (request, response) {
    var title = '달력화면';
    response.json({ "title": title })
});

// 달력일정 등록
router.post('/add', (req, res) => {
    var date = req.body.date;
    var content = req.body.content;
    var userId = req.body.userId;

    db.query('INSERT INTO calendar (cal_idx, userId, date, content) VALUES(?,?,?,?)', [null, userId, date, content], function (error, data) {
        if (error) throw error;
        errorcode = false
        res.json({
            "error": errorcode,
            "message": "일정이 등록되었습니다."
        })
    })
})

module.exports = router;