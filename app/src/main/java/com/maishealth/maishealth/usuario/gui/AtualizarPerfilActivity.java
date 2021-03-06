package com.maishealth.maishealth.usuario.gui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.maishealth.maishealth.R;
import com.maishealth.maishealth.infra.GuiUtil;
import com.maishealth.maishealth.usuario.dominio.Pessoa;
import com.maishealth.maishealth.usuario.negocio.Servicos;
import com.maishealth.maishealth.usuario.negocio.ServicosPessoa;
import com.maishealth.maishealth.usuario.negocio.ValidaCadastro;

import static com.maishealth.maishealth.infra.ConstanteSharedPreferences.DEFAULT_ID_USER_PREFERENCES;
import static com.maishealth.maishealth.infra.ConstanteSharedPreferences.DEFAULT_IS_MEDICO_PREFERENCES;
import static com.maishealth.maishealth.infra.ConstanteSharedPreferences.ID_USER_PREFERENCES;
import static com.maishealth.maishealth.infra.ConstanteSharedPreferences.IS_MEDICO_PREFERENCES;
import static com.maishealth.maishealth.infra.ConstanteSharedPreferences.TITLE_PREFERENCES;

public class AtualizarPerfilActivity extends AppCompatActivity {
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atualizar_perfil);
        TextView lblTextoNome = findViewById(R.id.textoNome);

        ServicosPessoa servicosPessoa = new ServicosPessoa(getApplicationContext());
        sharedPreferences = getSharedPreferences(TITLE_PREFERENCES, MODE_PRIVATE);
        long idUsuario = sharedPreferences.getLong(ID_USER_PREFERENCES, DEFAULT_ID_USER_PREFERENCES);

        if(idUsuario != DEFAULT_ID_USER_PREFERENCES){
            Pessoa pessoa = servicosPessoa.searchPessoaByIdUsuario(idUsuario);
            try{
                lblTextoNome.setText(pessoa.getNome());
            }catch (Exception e){
                Log.i("MenuPaciente", e.getMessage());
            }
        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.materialup_toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void mudarTela(Class tela){
        Intent intent=new Intent(this, tela);
        startActivity(intent);
        finish();
    }

    private void retornoMenuPrincipal(){
        SharedPreferences sharedPreferences = getSharedPreferences(TITLE_PREFERENCES, MODE_PRIVATE);
        if(sharedPreferences.getBoolean(IS_MEDICO_PREFERENCES, DEFAULT_IS_MEDICO_PREFERENCES)){
            this.mudarTela(MenuMedicoActivity.class);
        } else {
            this.mudarTela(MenuPaciente.class);
        }
    }

    public void retornoMenuPrincipal(View view){
        this.retornoMenuPrincipal();
    }

    @Override
    public void onBackPressed() {
        this.retornoMenuPrincipal();
    }

    public void onClickAtualizarPerfil(View view) {
        EditText edtEmail = findViewById(R.id.edtEmailMed);
        EditText edtSenha = findViewById(R.id.edtSenhaMed);
        String email = edtEmail.getText().toString();
        String senha = edtSenha.getText().toString();

        ValidaCadastro validaCadastro = new ValidaCadastro();
        boolean valido = true;
        boolean todosVazios = false;

        if(validaCadastro.isCampoVazio(email) && validaCadastro.isCampoVazio(senha)){
            GuiUtil.myToast(this, getString(R.string.promt_nenhum_campo_preenchido_perfil_nao_atualizado));
            valido = false;
            todosVazios = true;
        }

        if (!validaCadastro.isCampoVazio(senha) && !validaCadastro.isSenhaValida(senha)){
            edtSenha.requestFocus();
            edtSenha.setError(getString(R.string.error_invalid_password));
            valido = false;
        }

        if(!validaCadastro.isCampoVazio(email) && !validaCadastro.isEmail(email)) {
            edtEmail.requestFocus();
            edtEmail.setError(getString(R.string.error_invalid_email));
            valido = false;
        }
        if(valido && !todosVazios) {
            Servicos servicos = new Servicos(getApplicationContext());
            try{
                servicos.atualizarPerfil(email, senha);
                GuiUtil.myToast(this, getString(R.string.prompt_dados_atualizados));
                this.retornoMenuPrincipal();
            } catch (Exception e) {
                edtEmail.requestFocus();
                edtEmail.setText("");
                edtEmail.setError(e.getMessage());
            }
        }
    }
}