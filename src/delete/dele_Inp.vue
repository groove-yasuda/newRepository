<template>
    <v-app>
        <v-main>

            <v-container>
                <v-icon>mdi-account-minus</v-icon>
                <p>社員情報削除</p>
                <p>削除対象入力画面</p>


                    <v-text-field label="社員ID" clearable v-model="input_Emp_Id" @input="check_Emp_Id" counter="4"
                                  hint=" 社員IDをA001～Z999の間の半角英数字で入力して下さい。
                              *社員IDは大文字のアルファベット＋三桁の数字の組み合わせです"
                                  :rules="[id_Max_Char]" class="custom-hint-style"></v-text-field>
                    <p v-if="id_Error" class="error-message">有効な社員IDを入力してください。</p>

                    <v-text-field label="社員名" clearable v-model="input_Emp_Name" @input="check_Emp_Name" counter="255"
                                  hint="社員名を255文字以内の全角文字で入力して下さい"
                                  :rules="[name_Max_Char]" class="custom-hint-style"></v-text-field>
                    <p v-if="name_Error" class="error-message">有効な社員名を入力してください。</p>

            </v-container>
            <br>
            <v-row justify="center">
                <div>
                    <label for="year">生年月日:</label>
                    <select v-model="selected_Year" id="year" class="select-Dropdown">
                        <option v-for="year in year_Range" :value="year" :key="year">{{ year }}</option>
                    </select>年

                    <label for="month"></label>
                    <select v-model="selected_Month" id="month" class="select-Dropdown">
                        <option v-for="month in months" :value="month" :key="month">{{ month }}</option>
                    </select>月

                    <label for="day"></label>
                    <select v-model="selected_Day" id="day" class="select-Dropdown">
                        <option v-for="day in day_Range" :value="day" :key="day">{{ day }}</option>
                    </select>日
                </div>
            </v-row>
            <br><br>

                <v-row justify="center">
                    <v-col cols="auto">
                        <v-btn v-on:click="DELETE" :disabled="name_Error || id_Error ||
                           !input_Emp_Id || !input_Emp_Name">削除</v-btn>
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
    export default {
        data() {
            return {
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
                birth:'',
            };
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
            id_Max_Char(value) {
                if (value.length <= 4) {
                    return true;
                } else {
                    return '社員IDは4文字以下で入力してください';
                }
            },
            name_Max_Char(value) {
                if (value.length <= 255) {
                    return true; 
                } else {
                    return '社員名は255文字以下で入力してください'; 
                }
            },
            check_Emp_Id() {
                if (!/^[一-龯ぁ-んァ-ヶー]*$/.test(this.input_Emp_Id)) {
                    if (!/[A-Z]{1}[0-9]{3}/.test(this.input_Emp_Id) || !/^(?!.*000).+$/.test(this.input_Emp_Id)) {
                        this.id_Error = true;
                    } else {
                        this.id_Error = false;
                    }
                } else {
                    this.id_Error = true;
                }
            },
            check_Emp_Name() {
                if (!/^[一-龯ぁ-んァ-ヶー]*$/.test(this.input_Emp_Name)) {
                    this.name_Error = true;
                } else {
                    this.name_Error = false;
                }
            },

            DELETE() {
                this.birth = this.selected_Year + '/' + this.selected_Month + '/' + this.selected_Day;
                this.$router.push({ name: 'dele_Confi', params: { emp_Id: this.input_Emp_Id, emp_Name: this.input_Emp_Name,birth: this.birth } });
            },
        }
    }

</script>