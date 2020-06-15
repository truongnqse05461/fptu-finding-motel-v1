var registVue = new Vue({
    el: "#registerForm",
    data: {
        //flag
        matchPwd: true,
        matchOTP: true,
        noError: true,
        existedPhone: false,
        existedUsername: false,
        //message
        message: null,
        //parameter
        username: null,
        displayName: null,
        password: null,
        confirmPassword: null,
        phone: null,
        otpCode: null,
        role: null,
        //response
        otp: null,
        //request
        registerModel: {},
        //class css
        invisible: 'invisible',
        border_error: 'border_error',
        errorText: 'errorText',
    },
    methods: {
        checkMatchPwd: function (e) {
            if (this.password === this.confirmPassword) {
                this.matchPwd = true;
            } else {
                this.matchPwd = false;
            }
            return this.matchPwd;
        },
        checkOTP() {
            this.otp === this.otpCode ? this.matchOTP = true : this.matchOT = false;
            return this.matchOTP;
        },
        isExistUsername() {
            if (this.username != null && this.username.length !== 0) {
                fetch("http://localhost:8081/isExistUsername", {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json',
                    },
                    body: this.username,
                }).then(response => response.json())
                    .then((data) => {
                        console.log("username :" + data);
                        this.existedUsername = data;
                    }).catch(error => {
                    console.log(error);
                })
            } else {
                this.existedUsername = false;
            }

        },
        isExistPhone() {
            if (this.phone != null && this.phone.length !== 0) {
                fetch("http://localhost:8081/isExistPhone", {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json',
                    },
                    body: this.phone,
                }).then(response => response.json())
                    .then((data) => {
                        console.log("phone :" + data);
                        this.existedPhone = data;
                    }).catch(error => {
                    console.log(error);
                })
            } else {
                this.existedPhone = false;
            }

        },
        validRegister: function () {
            let registerModel = {
                "username": this.username,
                "role": this.role,
                "fbAccount": "",
                "ggAccount": "",
                "phoneNumber": this.phone,
                "password": this.password,
                "displayName": this.displayName,
            };
            if (this.checkMatchPwd() && this.checkOTP()) {
                fetch("http://localhost:8081/validRegister", {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json',
                    },
                    body: JSON.stringify(registerModel)
                }).then(response => response.json())
                    .then((data) => {
                        console.log(data);
                        if (data.code == '0') {
                            localStorage.setItem("registedUsername", this.username);
                            window.location.href = "/dang-nhap";
                        } else {
                            this.noError = false;
                            this.message = data.message;
                        }
                    }).catch(error => {
                    console.log(error);
                })
            }
        },
        sendOTP() {
            this.smsSendUrl = "http://rest.esms.vn/MainService.svc/json/SendMultipleMessage_V4_get?" +
                "ApiKey=A64092B4036FCBE98DC11D133598BA&SecretKey=4EB8AA82ED932ADD24FB776E928BFE&SmsType=2&Brandname=Verify";
            this.smsSendUrl += "&Phone=" + this.phone;//get from screen
            this.smsSendUrl += "&Content=Ma OTP cua ban la: " + this.otp;//get from screen
            fetch(this.smsSendUrl, {
                method: 'GET'
            }).then(response => response.json())
                .then((data) => {
                    this.smsResponse = data;
                })
        },
        getOTP() {
            if (this.phone == null || this.phone.length == 0) {
                alert("Hãy nhập số điện thoại!");
            } else {
                if (this.phone.length == 10) {
                    fetch("/api/get-otp?otpLength=6", {
                        method: 'POST'
                    })
                        .then(response => response.json())
                        .then((data) => {
                            this.otp = data;
                            this.sendOTP();
                        })
                } else {
                    alert("Hãy nhập số điện thoại chính xác!");
                }
            }
        }
    },
    computed: {}
})
