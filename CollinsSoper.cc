double calculateCosTheta(TLorentzVector Ele, TLorentzVector Elebar)
{
  //=***********************************************************************
  // Formula to Calculate Collins Soper
  // 1) cos(theta) = 2 Q^-1 (Q^2+Qt^2)^-1 (Ele^+ Elebar^- - Ele^- Elebar^+)
  //
  // ?) cos(theta) = (2/(Q.Mag()*sqrt(Q.Mag()^2 + Q.Pt()^2 
  //          * (Eleplus * Elebarminus - Eleminus * Elebarplus)
  //
  //
  //=***********************************************************************
  double Eleplus  = 1.0/sqrt(2.0) * (Ele.E() + Ele.Z());
  double Eleminus = 1.0/sqrt(2.0) * (Ele.E() - Ele.Z());

  double Elebarplus  = 1.0/sqrt(2.0) * (Elebar.E() + Elebar.Z());
  double Elebarminus = 1.0/sqrt(2.0) * (Elebar.E() - Elebar.Z());

  TLorentzVector Q(Ele+Elebar);

  double costheta = 2.0 / (Q.Mag() * sqrt(pow(Q.Mag(),2) + pow(Q.Pt(),2))) * (Eleplus * Elebarminus - Eleminus * Elebarplus);
  if (Q.Pz()<0.0) costheta = -costheta;
  return costheta;
}
