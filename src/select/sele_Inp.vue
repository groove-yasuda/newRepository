<template>
    <v-app>
        <v-main app class="custom-drawer">
            <v-container>
                <v-icon>mdi-account-search</v-icon>
                <p>社員情報検索</p>
                <p>入力画面</p>

                <input-field label="検索バー" v-model="input_Emp_Search" :hint="search_Hint" :error="Error" />
                <p>検索する文字は半角文字のみか全角文字のみのどちらかで入力して下さい</p>
                <p v-if="char_Error" class="error-message">文字数を255文字以内にしてください。</p>
                <p v-if="error_Message" class="error-message">{{ error_Message }}</p>

                <v-radio-group label="検索条件" v-model="search_Option">
                    <v-radio label="前方一致" value="front"></v-radio>
                    <v-radio label="後方一致" value="back"></v-radio>
                    <v-radio label="部分一致" value="part"></v-radio>
                    <v-radio label="全て一致" value="all"></v-radio>
                </v-radio-group>

            </v-container>



            <v-row justify="center">
                <v-col cols="auto">
                    <v-btn v-on:click="SEARCH">検索</v-btn>
                </v-col>
            </v-row>


        </v-main>
    </v-app>
</template>


<style type="text/css">
    p {
        font-family: 'Century', Times, serif;
        color:black;
    }
    .error-message {
        color: red;
    }
    .custom-drawer {
        background-color: white; /* 背景色の設定 */
    }
</style>

<script>
    import InputField from '@/components/input_Field.vue';

    export default {
        components: {
            InputField,
        },
        data() {
            return {
                search_Hint: '検索したい文字を255文字以内で入力して下さい',
                search_Option: 'front',
                input_Emp_Search: '', 
                search_Target: '',
                char_Error: false,
                Error: false,
                error_Message: '',
            };
        },
        created() {
            // ルーターからパラメータを取得してデータに代入
            this.error_Message = this.$route.params.error_Message;
        },
        methods: {
            SEARCH() {
                let hasError = false;

                if (this.input_Emp_Search.length >= 255) {
                    this.char_Error = true;
                    hasError = true;
                } else {
                    this.char_Error = false;
                }

                if (/^[ -~]/.test(this.input_Emp_Search)) {
                    this.search_Target = 'ID';
                } else {
                    this.search_Target = 'NAME';
                }

                if (!hasError) {
                    this.$router.push({
                        name: 'sele_Result', params: {
                            search_Option: this.search_Option, input_Emp_Search: this.input_Emp_Search, search_Target: this.search_Target
                        }
                    });
                }
            }
        },
    }

</script>