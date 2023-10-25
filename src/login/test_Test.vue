<template>
    <div>
        <br><br><br><br><br>
        <input v-model="textInput" />
        <button @click="validateAndProceed">次へ</button>
        <p v-if="errorMessage" class="error-message">{{ errorMessage }}</p>
    </div>
</template>

<script>
    export default {
        data() {
            return {
                textInput: "",
                errorMessage: ""
            };
        },
        methods: {
            validateAndProceed() {
                // バリデーションメソッドを呼び出す
                const validationResult = this.validateInput(this.textInput);

                if (validationResult === "valid") {
                    // バリデーションに合格した場合、次の画面に遷移
                    this.$router.push("/next-page");
                } else {
                    // エラーがある場合、エラーメッセージを設定
                    this.errorMessage = validationResult;
                }
            },
            validateInput(input) {
                // ここで文字数や形式のバリデーションを行う
                // エラーがない場合は "valid" を返し、エラーがある場合はエラーメッセージを返す
                if (input.length < 5) {
                    return "文字数が短すぎます";
                } else if (!/^[0-9]+$/.test(input)) {
                    return "数字のみが許可されています";
                } else {
                    return "valid";
                }
            }
        }
    };
</script>

<style>
    .error-message {
        color: red;
    }
</style>