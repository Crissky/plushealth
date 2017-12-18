package com.maishealth.maishealth.usuario.negocio;

import android.content.Context;

import com.maishealth.maishealth.usuario.dominio.Medico;
import com.maishealth.maishealth.usuario.persistencia.MedicoDAO;

public class ServicosMedico {
    private MedicoDAO medicoDAO;

    public ServicosMedico(Context context) {
        medicoDAO = new MedicoDAO(context);
    }

    public long cadastrarMedico(Medico medico){
        long idMedico = medicoDAO.inserirMedico(medico);

        return idMedico;
    }

    public long cadastrarMedico(String crm, String estado, String especialidade, long idUsuario) {
        Medico medico = new Medico();
        medico.setCrm(crm);
        medico.setEstado(estado);
        medico.setEspecialidade(especialidade);
        medico.setIdUsuario(idUsuario);

        return cadastrarMedico(medico);
    }
}