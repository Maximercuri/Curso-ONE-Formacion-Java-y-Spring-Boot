// URL base de la API
const baseURL = 'http://localhost:5500';

export default function getDatos(endpoint) {
    return fetch(`${baseURL}${endpoint}`)
        .then(response => response.json())
        .catch(error => {
            console.error('Error al acceder al endpoint /series/frases', error);
        });
}