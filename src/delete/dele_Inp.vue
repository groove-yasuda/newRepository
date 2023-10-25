<template>
    <v-app>
        <v-main>

            <v-container>
                <v-icon>mdi-account-minus</v-icon>
                <p>社員情報削除</p>
                <p>削除対象入力画面</p>

                <input-field label="社員ID" v-model="input_Emp_Id" :hint="Id_Hint" :error="id_Error" />
                <input-field label="社員名" v-model="input_Emp_Name" :hint="Name_Hint" :error="name_Error" />

                <p v-if="error_Message" class="error-message">{{ error_Message }}</p>


                <br>
                <v-row justify="center">
                    <div>
                        <select_Dropdown v-model="selected_Year"
                                         label="生年月日:" id="mySelect"
                                         selectClass="select-Dropdown"
                                         :options="year_Range" />
                    </div>
                    <div>
                        <select_Dropdown v-model="selected_Month"
                                         label="/" id="mySelect"
                                         selectClass="select-Dropdown"
                                         :options="months" />
                    </div>
                    <div>
                        <select_Dropdown v-model="selected_Day"
                                         label="/" id="mySelect"
                                         selectClass="select-Dropdown"
                                         :options="day_Range" />
                    </div>

                </v-row>
                <br><br>
                <v-row justify="center">
                    <v-col cols="auto">
                        <v-btn v-on:click="DELETE">削除</v-btn>
                    </v-col>
                </v-row>
            </v-container>
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
    .custom-hint-style .v-messages__message {
        font-size: 11px; /* ヒントの文字サイズを設定 */
        color: #333;
    }
    .select-Dropdown {
        padding: 5px; /* パディングを追加して選択肢が見やすくなります */
        border: 1px solid #ccc; /* ボーダーを追加してプルダウンの境界を明示的に表示します */
        border-radius: 4px; /* 角を丸くすることで視覚的に魅力的になります */
        background-color: white; /* 背景色を白に設定します */
        font-size: 18px; /* フォントサイズを調整できます */
        width: 65px; /* 幅を調整できます */
    }
</style>

<script>
    import InputField from '@/components/input_Field.vue';
    import select_Dropdown from '@/components/select_Dropdown.vue';

    export default {
        components: {
            InputField,
            select_Dropdown,
        },
        data() {
            return {
                Id_Hint: '社員IDをA001～Z999の間の半角英数字で入力して下さい * 社員IDは大文字のアルファベット＋三桁の数字の組み合わせです',
                Name_Hint: '社員名を255文字以内の全角文字で入力して下さい',
                input_Emp_Id: '',
                id_Error: false,
                input_Emp_Name: '', 
                name_Error: false,
                years: [1990], // 生年月日の年の選択肢
                months: [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12], // 生年月日の月の選択肢
                days: [1], // 生年月日の日の選択肢
                selected_Year: 2000, // 選択された年
                selected_Month: 1, // 選択された月
                selected_Day: 1, // 選択された日
                birth: '',
                error_Message: '',
            };
        },
        created() {
            // ルーターからパラメータを取得してデータに代入
            this.error_Message = this.$route.params.error_Message;
        },
        computed: {
            year_Range() {
                const current_Year = new Date().getFullYear();
                const start_Year = current_Year - 100; // 100年分の範囲を生成
                return Array.from({ length: 101 }, (_, i) => start_Year + i);
            },
            day_Range() {
                return Array.from({ length: 31 }, (_, i) => i + 1); // 日の選択肢を1から31まで生成
            },
        },
        methods: {
            DELETE() {
                const birth = this.selected_Year + '/' + this.selected_Month + '/' + this.selected_Day;
                let hasError = false;

                if (!/^[A-Z]{1}[0-9]{3}$/.test(this.input_Emp_Id) || !/^(?!.*000).+$/.test(this.input_Emp_Id)) {
                    this.id_Error = true;
                    hasError = true;
                } else {
                    this.id_Error = false;
                }

                if (!/^[一-龯ぁ-んァ-ヶー]*$/.test(this.input_Emp_Name)) {
                    this.name_Error = true;
                    hasError = true;
                } else {
                    this.name_Error = false;
                }

                if (!hasError) {
                    this.$router.push({ name: 'dele_Confi', params: { emp_Id: this.input_Emp_Id, emp_Name: this.input_Emp_Name, birth: birth } });
                }
            },
        }
    }

</script>