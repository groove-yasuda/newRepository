<template>
    <v-app>
        <v-main>
            <v-container>
                <v-icon>mdi-account-search</v-icon>
                <p>社員情報検索</p>
                <p>表示画面</p>


                <template>
                    <p v-if="isLoading && !is_Condition_Met">
                        ローディング中...
                    </p>

                    <p v-if="is_Condition_Met">
                        <v-container>
                            <v-row>
                                <v-col cols="12">
                                    <v-data-table :headers="headers"
                                                  :items="desserts"
                                                  :items-per-page="items_Per_Page"
                                                  class="elevation-10"
                                                  height="100px"
                                                  :footer-props="{
                                          showFirstLastPage: true,
                                          firstIcon: 'mdi-arrow-collapse-left',
                                          lastIcon: 'mdi-arrow-collapse-right',
                                          prevIcon: 'mdi-minus',
                                          nextIcon: 'mdi-plus'}">


                                        <template v-slot:item="{ item }">
                                            <tr>
                                                <td class="text-start" :style="{'color': 'black'}">{{ item['社員ID'] }}</td>
                                                <td class="text-start" :style="{'color': 'black'}">{{ item['社員名'] }}</td>
                                                <td class="text-start" :style="{'color': 'black'}">{{ item['生年月日'] }}</td>
                                                <td class="text-start" :style="{'color': 'black'}">{{ item['年齢'] }}</td>
                                                <td class="text-start" :style="{'color': 'black'}">{{ item['性別'] }}</td>
                                            </tr>
                                        </template>
                                    </v-data-table>
                                </v-col>
                            </v-row>

                            <v-row>
                                <v-col cols="12">
                                    <hot-table :data="attendance_Karam" :settings="set" colWidths="200" rowHeights="45"></hot-table>
                                    <hot-table :data="attendance_Data" :settings="settings" colWidths="200" rowHeights="45"></hot-table>
                                    <br>
                                    <hot-table :data="salary_Karam" :settings="set" colWidths="200" rowHeights="45"></hot-table>
                                    <hot-table :data="salary_Data" :settings="settings" colWidths="200" rowHeights="45"></hot-table>
                                    <br>
                                    <hot-table :data="deduction_Karam" :settings="set" colWidths="200" rowHeights="45"></hot-table>
                                    <hot-table :data="deduction_Data" :settings="settings" colWidths="200" rowHeights="45"></hot-table>
                                    <br>
                                    <hot-table :data="total_Karam" :settings="set" colWidths="200" rowHeights="45"></hot-table>
                                    <hot-table :data="total_Data" :settings="settings" colWidths="200" rowHeights="45"></hot-table>
                                </v-col>
                            </v-row>
                        </v-container>
                    </p>
                    <p v-if="!isLoading && !is_Condition_Met">検索結果が存在しません</p>
                </template>

            </v-container>


            <v-row justify="center">
                <v-col cols="auto">
                    <v-btn v-on:click="RETURN">戻る</v-btn>
                </v-col>
            </v-row>


        </v-main>
    </v-app>
</template>


<style type="text/css">
    p {
        font-family: 'Century', Times, serif;
        color: black;
    }

    .error-message {
        color: red;
    }

    .table-style {
        width: 75%;
        border-collapse: collapse;
    }

        .table-style th {
            padding: 8px;
            text-align: center;
            border: 5px solid #4e4e4e;
        }

        .table-style td {
            padding: 8px;
            text-align: center;
            border: 1px solid #4e4e4e;
        }
    .font-size {
         font-size: 20px; 
    }
</style>

<script>
    import { HotTable } from '@handsontable/vue';
    import { registerAllModules } from 'handsontable/registry';
    import Handsontable from 'handsontable';
    import 'handsontable/dist/handsontable.full.css';
    import axios from 'axios';

    registerAllModules();


    export default {
        data() {
            return {
                items_Per_Page: 1,
                search_Prime: '',
                search_Option: '',
                id_Order: '',
                name_Order: '',
                search_Target: '',
                response_Data: [],
                syainID: '',
                is_Condition_Met: '',
                isLoading: true,
                desserts: [],
                headers: [
                    { text: '社員ID' },
                    { text: '社員名' },
                    { text: '生年月日' },
                    { text: '年齢' },
                    { text: '性別' },
                ],
                attendance_Data: [
                    ["就業日数", "出勤日数", "労働時間", "欠勤日数",""],
                    [],
                    ["残業時間", "休日出勤日数"],
                    [],
                ],
                salary_Data: [
                    ["基本給", "通勤手当", "残業代", "固定残業代", "合計"],
                    [],
                ],
                deduction_Data: [
                    ["雇用保険", "健康保険", "厚生年金", "","社会保険合計"],
                    [],
                    ["所得税", "住民税", "", "", "税額合計"],
                    [],
                ],
                total_Data: [
                    ["総支給額", "", "控除総額","", "差引支給額"],
                    [],
                ],
                attendance_Karam: [
                    ["勤怠"],
                ],
                salary_Karam: [
                    ["給与"],
                ],
                deduction_Karam: [
                    ["控除"],
                ],
                total_Karam: [
                    ["合計"],
                ],
                settings: {
                    readOnly: true,
                    licenseKey: 'non-commercial-and-evaluation',
                    className: 'font-size',
                    cells: (row) => {
                        if (row === 0 || row === 2) {
                            return {
                                renderer: this.custom_Renderer,
                            };
                        }
                    },
                },
                set: {
                    readOnly: true,
                    licenseKey: 'non-commercial-and-evaluation',
                    className: 'font-size',
                    cells: (row) => {
                        if (row === 0 || row === 2) {
                            return {
                                renderer: this.custom_Karam_Renderer,
                            };
                        }
                    },
                },
            };
        },
        created() {
            // ルーターからパラメータを取得してデータに代入
            this.syainID = this.$route.params.syainID;
            console.log('クリックされた行の社員ID:', this.syainID);
            // ルーターからパラメータを取得してデータに代入
            this.search_Prime = this.$route.params.search_Prime;
            this.search_Option = this.$route.params.search_Option;
            this.id_Order = this.$route.params.id_Order;
            this.name_Order = this.$route.params.name_Order;
            this.input_Emp_Search = this.$route.params.input_Emp_Search;
            this.search_Target = this.$route.params.search_Target;
        },
        mounted() {
            axios
                .request({
                    method: 'POST',
                    url: 'http://localhost:8080/select_Private_Func',
                    data: {
                        search_Prime: 'syainID',
                        search_Option: 'front',
                        id_Order: 'ASC',
                        name_Order: 'ASC',
                        input_Emp_Search: this.syainID,
                        search_Target: 'ID'
                    }
                })
                .then((response) => {
                    if (response.data && "key0" in response.data && "key1" in response.data) {
                        const data = response.data; // レスポンスデータを取得

                        const list_Employee_Information = data["key0"]; // "key0"のリストを取得
                        const list_Deduction = data["key1"]; // "key1"のリストを取得

                        let sousikyu;
                        let Age;


                        list_Employee_Information.forEach((item) => {
                            const parts = item.split(", ");
                            const syainID = parts[0].split("=")[1];
                            const syainNAME = parts[1].split("=")[1];
                            const birth = parts[2].split("=")[1];
                            const age = parts[3].split("=")[1];
                            const gender = parts[4].split("=")[1];

                            this.desserts.push({
                                '社員ID': syainID,
                                '社員名': syainNAME,
                                '生年月日': birth,
                                '年齢': age,
                                '性別': gender,
                            });

                            const basic_salary = parseFloat(parts[5].split("=")[1]);
                            const Transportation_expenses = parseFloat(parts[6].split("=")[1]);
                            const overtime_pay = parseFloat(parts[7].split("=")[1]);
                            const Fixed_overtime_pay = parseFloat(parts[8].split("=")[1]);

                            this.salary_Data[1][0] = `¥${basic_salary.toLocaleString()}`;
                            this.salary_Data[1][1] = `¥${Transportation_expenses.toLocaleString()}`;
                            this.salary_Data[1][2] = `¥${overtime_pay.toLocaleString()}`;
                            this.salary_Data[1][3] = `¥${Fixed_overtime_pay.toLocaleString()}`;
                            this.salary_Data[1][4] = `¥${(basic_salary + Transportation_expenses + overtime_pay + Fixed_overtime_pay).toLocaleString()}`;

                            sousikyu = basic_salary + Transportation_expenses + overtime_pay + Fixed_overtime_pay;
                            Age = age;



                            const employment = parseFloat(parts[9].split("=")[1]);
                            const attendance_At_Work = parseFloat(parts[10].split("=")[1]);
                            const work = parts[11].split("=")[1];
                            const absenteeism = parseFloat(parts[12].split("=")[1]);
                            const overtime = parts[13].split("=")[1];
                            const holiday_Work = parts[14].split("=")[1];




                            this.is_Condition_Met = true;
                            this.isLoading = false;

                            this.attendance_Data[1][0] = employment + ' 日';
                            this.attendance_Data[1][1] = attendance_At_Work + ' 日';
                            this.attendance_Data[1][2] = work + ' 時間';
                            this.attendance_Data[1][3] = holiday_Work + ' 日';
                            this.attendance_Data[3][0] = overtime + ' 時間';
                            this.attendance_Data[3][1] = absenteeism + ' 日';
                        });

                        list_Deduction.forEach((item) => {
                            const parts = item.split(", ");
                            const employment_Insurance_Rate = parseFloat(parts[0].split("=")[1]);
                            const welfare_Pension_Insurancehoken = parseFloat(parts[1].split("=")[1]);
                            const insurance_Rate_Nursing = parseFloat(parts[2].split("=")[1]);
                            const insurance_Rate_Medical = parseFloat(parts[3].split("=")[1]);
                            const income_Tax_Basis = parseFloat(parts[4].split("=")[1]);
                            const resident_Tax_Basics = parseFloat(parts[5].split("=")[1]);
                            const income_Tax_Rate = parseFloat(parts[6].split("=")[1]);
                            const resident_Tax_Rate = parseFloat(parts[7].split("=")[1]);
                            const deduction = parseFloat(parts[8].split("=")[1]);
                            const employment_Insurance = sousikyu * employment_Insurance_Rate * 0.01;
                            let health_Insurance_Nursing = 0;
                            if (Age >= 40) {
                                health_Insurance_Nursing = sousikyu * insurance_Rate_Nursing * 0.5 * 0.01;
                            }
                            const health_Insurance_Medical = sousikyu * insurance_Rate_Medical * 0.5 * 0.01;
                            const health_Insurance = health_Insurance_Nursing + health_Insurance_Medical;
                            const welfare_Pension_Insurance = sousikyu * welfare_Pension_Insurancehoken * 0.5 * 0.01;//
                            const employment_Income = (sousikyu * 12) - deduction;
                            const taxable_Salary_Income = (employment_Income - income_Tax_Basis - (welfare_Pension_Insurance * 12) - (health_Insurance * 12) - (employment_Insurance * 12)) / 12;
                            const income_Tax = taxable_Salary_Income * income_Tax_Rate * 0.01;
                            const taxable_Salary_residents = (employment_Income - resident_Tax_Basics - (welfare_Pension_Insurance * 12) - (health_Insurance * 12) - (employment_Insurance * 12)) / 12;
                            const resident_Tax = taxable_Salary_residents * resident_Tax_Rate * 0.01;
                            const social_Insurance = employment_Insurance + health_Insurance + welfare_Pension_Insurance;
                            const tax_Amount = income_Tax + resident_Tax;
                            const deduction_Amount = social_Insurance + tax_Amount;
                            const deduction_Payment_Amount = sousikyu - deduction_Amount;

                            this.deduction_Data[1][0] = `¥${employment_Insurance.toLocaleString()}`;//雇用保険
                            this.deduction_Data[1][1] = `¥${health_Insurance.toLocaleString()}`;//健康保険
                            this.deduction_Data[1][2] = `¥${Math.ceil(welfare_Pension_Insurance).toLocaleString()}`;//厚生年金
                            this.deduction_Data[1][4] = `¥${Math.ceil(social_Insurance).toLocaleString()}`;//社会保険合計
                            this.deduction_Data[3][0] = `¥${Math.ceil(income_Tax).toLocaleString()}`;//所得税
                            this.deduction_Data[3][1] = `¥${Math.ceil(resident_Tax).toLocaleString()}`;//住民税
                            this.deduction_Data[3][4] = `¥${Math.ceil(tax_Amount).toLocaleString()}`;//税額合計

                            this.total_Data[1][0] = `¥${Math.ceil(sousikyu).toLocaleString()}`;//総支給額
                            this.total_Data[1][2] = `¥${Math.ceil(deduction_Amount).toLocaleString()}`;//税額合計
                            this.total_Data[1][4] = `¥${Math.ceil(deduction_Payment_Amount).toLocaleString()}`;//税額合計

                        });


                    } else {
                        this.isLoading = false;
                        this.is_Condition_Met = false;
                    }
                })

        },
        methods: {

            RETURN() {
                this.$router.push({
                    name: 'sele_Result', params: {
                        search_Prime: this.search_Prime, search_Option: this.search_Option,
                        id_Order: this.id_Order, name_Order: this.name_Order,
                        input_Emp_Search: this.input_Emp_Search, search_Target: this.search_Target
                    }
                });
            },
            custom_Renderer(instance, td) {
                Handsontable.renderers.TextRenderer.apply(this, arguments);
                td.className = 'custom-cell'; 
                td.style.backgroundColor = 'gray'; 
                td.style.color = 'black'; 
                td.style.fontSize = '20px';
            },
            custom_Karam_Renderer(instance, td) {
                Handsontable.renderers.TextRenderer.apply(this, arguments);
                td.className = 'custom-cell'; 
                td.style.backgroundColor = 'silver'; 
                td.style.color = 'black'; 
                td.style.fontSize = '20px';
            },
        },
        components: {
            HotTable,
        },
    }

</script>