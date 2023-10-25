<template>
    <v-app>
        <v-main>
            <v-container>
                <v-icon>mdi-account-minus</v-icon>
                <p>社員情報登削除</p>
                <p>確認画面</p>
                <p>以下の社員情報を削除します。</p>
                <p>間違いがなければ確認ボタンを押してください</p>
                <p class="text-center" :style="{'color': 'black'}">社員ID : {{ emp_Id }}</p>
                <p class="text-center" :style="{'color': 'black'}">社員名 : {{ emp_Name }}</p>
                <p class="text-center" :style="{'color': 'black'}">生年月日 : {{ birth }}</p>

            </v-container>

            <v-row justify="center">
                <v-col cols="auto">
                    <v-btn v-on:click="RETURN">戻る</v-btn>
                </v-col>
                <v-col cols="auto">
                    <v-spacer></v-spacer>
                </v-col>
                <v-col cols="auto">
                    <v-btn v-on:click="CONFI">確認</v-btn>
                </v-col>
            </v-row>


        </v-main>
    </v-app>
</template>

<script>
    import axios from 'axios';
    export default {
        data() {
            return {
                emp_Id: '', // 社員IDを保持するデータ
                emp_Name: '', // 社員名を保持するデータ
                birth: '', // 生年月日を保持するデータ
            };
        },
        created() {
            // ルーターからパラメータを取得してデータに代入
            this.emp_Id = this.$route.params.emp_Id;
            this.emp_Name = this.$route.params.emp_Name;
            this.birth = this.$route.params.birth;
        }, 
        methods: {
            RETURN() {
                this.$router.push({ name: 'dele_Inp' });
            },
            CONFI() {
                axios
                    .request({
                        method: 'POST',
                        url: 'http://localhost:8080/delete_Func',
                        data: {
                            id_Over: this.emp_Id,
                            name_Over: this.emp_Name,
                            birth: this.birth,
                        }
                    })
                    .then((response) => {
                        if (response.data === true) {
                            this.$router.push({ name: 'dele_Comp' });
                        }
                        else if (response.data === false) {
                            const error_Message = '入力された社員情報は登録されていません。';
                            this.$router.push({ name: 'dele_Inp', params: { error_Message: error_Message } });
                        }

                    })
            },
        }
    };
</script>