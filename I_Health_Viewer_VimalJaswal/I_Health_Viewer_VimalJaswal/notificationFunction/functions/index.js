const functions = require('firebase-functions');
const admin = require('firebase-admin');


admin.initializeApp();


exports.sendNotifications = functions.firestore.document('patients/{patient_id}/notifications/{notification_id}').onWrite(async (change, context) => {

    const patient_id = await context.params.patient_id;
    const notification_id = await context.params.notification_id;
    const doctor_id = 'andy@gmail.com';

    // console.log('patient_id: ' +patient_id+ ' | notification_id: ' +notification_id+ ' | doctor_id: ' +doctor_id+' | patientNotification_id: '+patientNotification_id);

    return admin.firestore().collection('patients').doc(patient_id).collection('notifications').doc(notification_id)
    .get().then(async (queryResult) => {
        
        const from_data = await admin.firestore().collection('patients').doc(patient_id).collection('notifications').doc(doctor_id).get();
        const to_data = await admin.firestore().collection('doctors').doc(doctor_id).get();

        return Promise.all([from_data, to_data]).then(async (result) => {


            const from_name = await result[0].data().userName;
            const to_name = await result[1].data().name;
            const from_message = await result[0].data().heartRate;
            

            const to_token_id = await result[1].data().token_id;
            



            const payload = {
                notification: {
                    title: "Patient Name: " + from_name,
                    body:  "Vitals: " + from_message,
                }
            };
        
     
           
            return admin.messaging().sendToDevice(to_token_id, payload).then((result) => {

                return console.log("Notifcation sent.");
            });

            // return console.log("From: " + from_name + " To: " + to_name + " Heart Rate: " + from_message);
        });

    });
    
});