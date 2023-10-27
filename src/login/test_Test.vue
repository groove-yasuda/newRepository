<template>
    <v-app>
        <v-main>
            <v-container>

                <p>社員情報登録</p>
                <p>社員情報入力画面</p>


                <br>
                <v-row justify="center" class="a">
                    <p>社員情報入力画面</p>
                </v-row>
                <br><br>

                <v-row justify="center" class="a">
                    <v-col cols="auto" class="a">
                        <v-btn v-on:click="INSERT">登録</v-btn>
                    </v-col>
                </v-row>


            </v-container>



        </v-main>
    </v-app>
</template>



<style>
    .a{
        width: 80%;
        color: red;
    }
    .error-message {
        color: red;
    }
</style>