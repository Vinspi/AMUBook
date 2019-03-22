var fs = require('fs');

var names;

function getRandomInt(max) {
    return Math.floor(Math.random() * Math.floor(max));
}

function getRandomName() {
    return names[getRandomInt(names.length-1)];
}

names = fs.readFileSync('MOCK_DATA.csv', 'utf8');


// var surname;
// var name;

// insert into CV (id, description, titre) values (10, 'Monroe', 'Kleinfeld');


names = names.split('\n');

// console.log(names);
for(var i=0;i<100000;i++) {
    console.log("insert into CV (id, description, titre) values ("+i+", 'Default description', 'Test');");
}
console.log('\n\n');

for(var i=0;i<100000;i++) {
    name = getRandomName();
    surname = getRandomName();
    console.log("insert into Personne (id, birthdate, email, nom, prenom, password, salt, valide, website, cv_id) values ("+i+", '1933-06-21 18:49:06', '"+name+"."+surname+"@dropbox"+i+".com', '"+name+"', '"+surname+"', x'0c11e65c26f46cb8f7898926e9559bce8e6c08e956526336683983c2af5af15c35591c8d3e64a59eedca929abce0c7804642ff5778d2b96adb6920bae65265a6', x'73656c', true, 'https://photobucket.com', "+i+");");
    
}


