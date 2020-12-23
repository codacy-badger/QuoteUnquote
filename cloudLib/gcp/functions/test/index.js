'use strict';

const app = require('../index');

const chaiHttp = require("chai-http");

const chai = require("chai");
var expect = chai.expect;

chai.use(chaiHttp);

describe('========================= test suite - /save', () => {
    it("invalid code - length", done => {
        chai
            .request(app)
            .post('/save')
            .send({ 
                "code": "012345678", 
                "digests": ["d1", "d2"] 
            })
            .end((err, res) => {
                expect(res).to.have.status(400);
                expect(res.type).to.equal("application/json");
                expect(res.text).to.equal("{\"error\":\"JSON not valid\"}");
                done();
            });
    });

    it("invalid code - crc", done => {
        chai
            .request(app)
            .post('/save')
            .send({ 
                "code": "0123456789", 
                "digests": ["d1", "d2"] 
            })
            .end((err, res) => {
                expect(res).to.have.status(400);
                expect(res.type).to.equal("application/json");
                expect(res.text).to.equal("{\"error\":\"JSON not valid\"}");
                done();
            });
    });

    it("invalid digests", done => {
        chai
            .request(app)
            .post('/save')
            .send({ 
                "code": "012345672e",
                "digest": ["d1", "d2"] 
            })
            .end((err, res) => {
                expect(res).to.have.status(400);
                expect(res.type).to.equal("application/json");
                expect(res.text).to.equal("{\"error\":\"JSON not valid\"}");
                done();
            });
    });

    it("valid", done => {
        chai
            .request(app)
            .post('/save')
            .send({ 
                "code": "012345672e", 
                "digests": ["d1", "d2"] 
            })
            .end((err, res) => {
                expect(res.type).to.equal("text/plain");
                expect(res).to.have.status(200);
                done();
            });
    });
});

describe('========================= test suite - /receive', () => {
    it("invalid - code missing", done => {
        chai
            .request(app)
            .post('/receive')
            .send({ 
                "": "012345672e"})
            .end((err, res) => {
                expect(res).to.have.status(400);
                expect(res.type).to.equal("application/json");
                expect(res.text).to.equal("{\"error\":\"JSON not valid\"}");
                done();
            });
    }).timeout(10000);

    it("valid request", done => {
        chai
            .request(app)
            .post('/receive')
            .send({ 
                "code": "012345672e",
            })
            .end((err, res) => {
                expect(res).to.have.status(200);
                expect(res.type).to.equal("application/json");
                
                // receiveFromBucket (escaped), but real problem is that bucket is slow
                // expect(res.text).to.equal('"[\\"d1\\",\\"d2\\"]"');

                // receiveFromFirebase
                expect(res.text).to.equal('["d1","d2"]');
                done();
            });
    }).timeout(10000);
});
